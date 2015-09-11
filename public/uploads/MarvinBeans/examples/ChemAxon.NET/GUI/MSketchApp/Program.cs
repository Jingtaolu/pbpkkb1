using System;
using System.Collections;
using System.Windows.Forms;
using System.Diagnostics;
using System.IO;
using com.jnbridge.jnbproxy;

namespace chemaxon.jnbridge.marvin.example
{
    class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            /*
            string currDirectory = Environment.CurrentDirectory;
            string jvmLocation = getJvmLocation(currDirectory);
            string jnbcoreLocation = currDirectory + "\\jnbcore.jar";
            string bcelLocation = currDirectory + "\\bcel-5.1-jnbridge.jar";
            string classpath = currDirectory + "\\jnbtools.jar;" +
                currDirectory + "\\jchem.jar;" +
                currDirectory + "\\dom4j.jar";
            String[] jvmOptions = new String[0];

            JNBRemotingConfiguration.specifyRemotingConfiguration(
                JavaScheme.sharedmem,
                jvmLocation,
                jnbcoreLocation,
                bcelLocation,
                classpath,
                jvmOptions);
            */

            Application.EnableVisualStyles();
            Application.Run(new MSketch());
        }

        static string getJvmLocation(string currDirectory)
        {
            string pathToConfigFile = currDirectory + "\\MSketchApp.config.txt";

            using (StreamReader sr = new StreamReader(pathToConfigFile))
            {
                String line;
                // Read and display lines from the file until the end of 
                // the file is reached.
                while ((line = sr.ReadLine()) != null)
                {
                    return line;
                }
            }
            return null;
        }

        public static void debug(string msg)
        {
            MessageBox.Show(msg, "DEBUG",
                            MessageBoxButtons.OK,
                            MessageBoxIcon.Exclamation);
        }
    }
}
