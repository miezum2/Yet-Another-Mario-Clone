
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.net.URL;
import java.net.URLClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.ArrayList;

/**
 * <p>This is for loading classes which use a native library.</p>
 * 
 * <p>Greenfoot uses classloaders to allow the same class to be loaded multiple times.
 * However if a class loads a native library, then that will be loaded multiple times.
 * Loading a native library multiple times is not allowed by the JVM, and so it will
 * fail.</p>
 * 
 * <p>This solution works by manually loading classes using the root classloader,
 * which is the parent to all of Greenfoots classloaders. By doing this the class is
 * only ever loaded once and is accessible to all of the child classloaders.</p>
 * 
 * @author Joseph Lenton
 */
public class NativeLoader  
{
    // this extension must be kept in lowercase
    private final static String CLASS_FILE_EXTENSION = ".class";
    
    private final static File PROJECT_FOLDER = new File(".");
    
    private final URLClassLoader rootCL;
    private final Method defineClass;
    private final Method addUrlMethod;
    private final Method findClassMethod;
    
    private URL[] rootCLUrls;
    
    /**
     * Trivial constructor.
     */
    public NativeLoader()
    {
        // find the top parent class loader
        ClassLoader parentCL = null;
        for ( ClassLoader cl = getClass().getClassLoader(); cl != null; cl = cl.getParent() ) {
            parentCL = cl;
        }
        
        if ( ! (parentCL instanceof URLClassLoader) ) {
            throw new IllegalStateException("Java hack of casting system classloader to an URLClassLoader is no longer working.");
        }
        
        this.rootCL = (URLClassLoader) parentCL;
        
        Method defineClass = null;
        for ( Method m : ClassLoader.class.getDeclaredMethods() ) {
            if ( m.getName().equals("defineClass") && m.getParameterTypes().length == 4 ) {
                defineClass = m;
                break;
            }
        }
        defineClass.setAccessible( true );
        this.defineClass = defineClass;
        
        try {
            this.findClassMethod = ClassLoader.class.getDeclaredMethod( "findClass", String.class );
            this.findClassMethod.setAccessible( true );
            
            this.addUrlMethod = URLClassLoader.class.getDeclaredMethod(
                    "addURL",
                    new Class[] { URL.class }
            );
            this.addUrlMethod.setAccessible( true );
        } catch ( NoSuchMethodException ex ) {
            throw new RuntimeException( ex );
        }
        
        this.rootCLUrls = null;
    }
    
    /**
     * <p>Looks for and then loads the class given using the root class loader.</p>
     * 
     * <p>The given name should be the name of a java class, such as: net.foo.MyClass</p>
     * 
     * <p>If the class is in the default package then it is presumed to be residing inside
     * of the Greenfoot project folder.</p>
     * 
     * @param The Java name of the class to load.
     */
    public void loadClass( final String name )
    {
        try {
            rootCL.loadClass( name );
        } catch ( ClassNotFoundException ex ) {
            final String lowerClassFileName = (name + NativeLoader.CLASS_FILE_EXTENSION).toLowerCase();
            // Check if it's in the current directory, then it's in the default package.
            // Case insensetive search for the file.
            for ( final File file : NativeLoader.PROJECT_FOLDER.listFiles() ) {
                if ( lowerClassFileName.equals( file.getName().toLowerCase() ) && file.isFile() ) {
                    loadClassFile( file );
                    return;
                }
            }
            
            // this point is reached only if class is not loaded
            throw new RuntimeException( ex );
        }
    }
    
    /**
     * <p>The same as the other addClasspathDirectory method,
     * only this takes a string for the directory to add rather then a File object.</p>
     * 
     * @param directory The directory to add to the root classloaders classpath.
     */
    public void addClasspath( String directory )
    {
        addClasspath( new File(directory) );
    }
    
    /**
     * <p>This is for adding directories to check when a loaded class is searching for classes.</p>
     * 
     * <p>The idea is that if you have a class loaded using the root classloader,
     * when that class imports other non-loaded classes then you want them to be found.
     * That can be done by adding the folder where those classes are found to the
     * root classloader. This method allows you to do that.</p>
     * 
     * <p>Note that this only has an effect when the JVM searches itself for a class;
     * it has no effect on where the methods 'loadClass' and 'loadClasses' search.</p>
     * 
     * <p>You cannot add the Greenfoot project folder to the classpath (which is
     * located at '.') as this will cause Greenfoot to crash.</p>
     * 
     * <p>You can also pass in a .jar file instead of a directory to add that to the class patth.</p>
     * 
     * @param dir The directory to add to the root classloaders classpath, cannot be null.
     */
    public void addClasspath( File dir )
    {
        if ( dir == null ) {
            throw new IllegalArgumentException("Given file cannot be null.");
        } else if ( !dir.exists() ) {
            throw new IllegalArgumentException("Given file or directory cannot be found: " + dir.getPath());
        }
        
        // adding project folder breaks Greenfoot, so just silently return.
        if ( ! dir.equals(PROJECT_FOLDER) ) {
            try {
                final URL newUrl = dir.toURI().toURL();
                if ( this.rootCLUrls == null ) {
                    this.rootCLUrls = rootCL.getURLs();
                }
                boolean addUrl = true;
                
                for ( int i = 0; i < this.rootCLUrls.length; i++ ) {
                    if ( this.rootCLUrls[i].equals(newUrl) ) {
                        addUrl = false;
                        break;
                    }
                }
                
                if ( addUrl ) {
                    try {
                        addUrlMethod.invoke( this.rootCL, newUrl );
                        this.rootCLUrls = null;
                    } catch ( Exception ex ) {
                        throw new RuntimeException("Error, could not add URL to system classloader.");
                    }
                }
            } catch ( IOException ex ) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * <p>The same as the other loadDirectory method, only this sets it to load recursively.</p>
     * 
     * @param directory The path to the directory to load.
     */
    private void loadDirectory( String directory )
    {
        loadDirectory( directory, true );
    }
    
    /**
     * <p>The same as the loadDirectory which takes a File object, only this
     * takes the path to that directory as a String instead of instead a File.</p>
     * 
     * @param directory The directory to load classes from.
     * @param isRecursive True to check the directories within the one given, and load their contents too. False to not.
     */
    private void loadDirectory( String directory, boolean isRecursive )
    {
        if ( directory == null ) {
            throw new IllegalArgumentException("directory cannot be null.");
        }
        
        loadDirectory( new File(directory), isRecursive );
    }
    
    /**
     * <p>This loads all of the java class files found in the directory given
     * using the root classloader. This also adds that directory to the classpath
     * of the root classloader.<p>
     * 
     * <p>The directory given is relative to the location of this Greenfoot project.</p>
     * 
     * <p>If isRecursive is true then the same is performed on any directories found
     * within the one given. If it is false then those directories are ignored.</p>
     * 
     * @param directory The directory to load all class files from.
     * @param isRecursive True to perform the operations recursively on the directory, false to not.
     */
    private void loadDirectory( File directory, boolean isRecursive )
    {
        if ( directory == null ) {
            throw new IllegalArgumentException("directory cannot be null.");
        } else if ( ! directory.exists() ) {
            throw new IllegalArgumentException("directory given does not exist.");
        } else if ( ! directory.isDirectory() ) {
            throw new IllegalArgumentException("Given directory is not a directory.");
        }
        
        addClasspath( directory );
        
        for ( final File file : directory.listFiles() ) {
            if ( file.isFile() && isClassFile(file) ) {
                loadClassFile( file );
            } else if ( isRecursive && file.isDirectory() ) {
                loadDirectory( file, isRecursive );
            }
        }
    }
    
    /**
     * <p>The given file is a class file if it's name ends with '.class'.</p>
     * 
     * @param file The file to test if it is or isn't a class file, it is expected not to be null.
     * @return True if the file is a class file, otherwise false.
     */
    private boolean isClassFile( File file )
    {
        return file.getName().toLowerCase().endsWith( CLASS_FILE_EXTENSION );
    }
    
    /**
     * <p>The same as loadClassFile, except you can pass in multiple paths to this one.</p>
     * 
     * @param klasses Paths to all of the .class files to load.
     */
    private void loadClassFiles( String ... klasses )
    {
        for ( int i = 0; i < klasses.length; i++ ) {
            this.loadClassFile( klasses[i] );
        }
    }
    
    /**
     * <p>The same as the other loadClassFile method, only this takes a String path
     * to the file to load.</p>
     * 
     * @param klassPath A path to a .class file to load using the root classloader, cannot be null.
     */
    private void loadClassFile( String klassPath )
    {
        loadClassFile( new File(klassPath) );
    }
    
    /**
     * <p>The same as the other loadClassFile method (the one that takes an InputStream)
     * except that this accepts a File object instead. See that one for more information.<p>
     * 
     * @param file A .class file to load using the root classloader, cannot be null.
     */
    private void loadClassFile( File file )
    {
        if ( file == null ) {
            throw new IllegalArgumentException("The given file cannot be null.");
        } else if ( ! file.exists() ) {
            throw new IllegalArgumentException("The given klass cannot be found, searched at:" + file.getAbsolutePath() );
        } else if ( ! file.isFile() ) {
            throw new IllegalArgumentException("The given klass is not a file, searched at:" + file.getAbsolutePath() );
        }
        
        try {
            this.loadClassFile( new FileInputStream(file) );
        } catch ( FileNotFoundException ex ) {
            throw new RuntimeException( ex );
        }
    }
    
    /**
     * <p>This loads the .class file that can be read from the given input stream.
     * It is fully read in and then loaded manually with the root classloader.</p>
     * 
     * <p>What this does is to ensure that the class is only ever loaded once,
     * and then shared across every compilation of your Greenfoot project.</p>
     * 
     * @param input The InputStream to read a .class file from, cannot be null.
     */
    private void loadClassFile( InputStream input )
    {
        if ( input == null ) {
            throw new IllegalArgumentException("The given input cannot be null.");
        }
        
        try {
            final String klass = load( input );
            
            if ( klass != null ) {
                rootCL.loadClass( klass );
            }
        } catch ( ClassNotFoundException ex ) {
            throw new RuntimeException( ex );
        }
    }
    
    /**
     * <p>This does the actual loading. It reads the .class file from the given InputStream and then
     * attempts to define it as a class using the root classloader.</p>
     * 
     * @param input An InputStream containing a .class file, cannot be null.
     */
    private String load( InputStream input )
    {
        try {
            final byte[] klassBytes = inputToBytes( input );
            final Class<?> klass = (Class<?>) this.defineClass.invoke( rootCL, null, klassBytes, 0, klassBytes.length );
            return klass.getName();
        } catch ( IOException ex ) {
            throw new RuntimeException( "Error reading in class.", ex );
        } catch ( InvocationTargetException ex ) {
            // Ignore LinkageErrors, they are expected if class is loaded on second attempt.
            // So if is "if the reflective call above failed for an unknown reason"...
            if ( ! (ex.getCause() instanceof LinkageError) ) {
                throw new RuntimeException( ex );
            }
            
            return null;
        } catch ( Exception ex ) {
            throw new RuntimeException( ex );
        }
    }
    
    /**
     * <p>Reads everything available from the given InputStream into one byte array and returns it.</p>
     * 
     * @return The contents of the given InputStream in one giant byte array.
     */
    private byte[] inputToBytes( InputStream input )
            throws Exception
    {
        final List<byte[]> bytes = new ArrayList<byte[]>();
        final List<Integer> sizes = new ArrayList<Integer>();
        int totalSize = 0;
        int available = 0;
        
        // store all bytes from input
        while ( (available = input.available()) > 0 ) {
            final byte[] nextBytes = new byte[ available ];
            final int read = input.read( nextBytes );
            bytes.add( nextBytes );
            sizes.add( read );
            totalSize += read;
        }
        
        // compact into one big byte array
        final byte[] returnBytes = new byte[ totalSize ];
        int index = 0;
        for ( int i = 0; i < bytes.size(); i++ ) {
            final byte[] bs = bytes.get( i );
            final int size = sizes.get( i );
            
            System.arraycopy( bs, 0, returnBytes, index, size );
        }
        
        input.close();
        
        return returnBytes;
    }
}
