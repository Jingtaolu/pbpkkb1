<%--

    createsaveimage.jsp

    ChemAxon Ltd., 1999-2008

    Generates an image, save it and sends its URL to the web browser.
    
    Parameters:
    
    format	    image format 
		    details: http://www.chemaxon.com/marvin/help/formats/images-doc.html
		    
    mol		    input structure
		    details: http://www.chemaxon.com/marvin/help/formats/formats.html

--%>

<%@page import="java.io.*,java.net.URLDecoder,chemaxon.formats.MolImporter,chemaxon.struc.Molecule,chemaxon.formats.MolExporter"%>
<%
// absolute path of the working directory
String workdir = "/var/www/public/test/image-generation/workdir";
// relative url of the working directory
String workdirurl = "workdir";
try {
    // Retrieving GET/POST parameters
    String format = request.getParameter("format");
    if(format == null) {
    	throw new Exception("Format needs to be specified.");
    }
    String molstring = request.getParameter("mol");
    if(molstring == null) {
	throw new Exception("Structure needs to be specified.");
    }

    // Setting content type
    String type = (format.indexOf(":") == -1)? format : 
            format.substring(0, format.indexOf(":"));

    response.setContentType("text/plain");

    // Creating the molecule
    Molecule mol = null;
    // Reading the posted SMILES string
    mol = MolImporter.importMol(molstring);
    
    // Calculate the coordinates if needed (for example
    // if the input is SMILES)
    if(mol.getDim()==0) {
        mol.clean(2, "O1");
    }
    
    // specify outputfile
    String outputfilename = "output."+type;
    File parent = new File(workdir);
    File f = new File(parent,outputfilename);
    // output url
    // assign a unique query string to image URL to force browser
    // to reload image by each request
    String search = "?"+Long.toString(System.currentTimeMillis());
    String outputurlstr = workdirurl+"/"+outputfilename+search;
    try {
        // Generating the image
        byte[] b = MolExporter.exportToBinFormat(mol, format);
        // save the image
        f.delete();
        FileOutputStream fout = new FileOutputStream(f);
        BufferedOutputStream bout = new BufferedOutputStream(fout);
        bout.flush();
        bout.write(b,0,b.length);
        bout.close();
        b = outputurlstr.getBytes();
    } catch(IOException ioex) {
        // the error message will sent back
        b = (ioex.getMessage() != null)? ioex.getMessage().getBytes() : 
            new String("unknow io error by saving "+outputfilename).getBytes();
    }
    ServletOutputStream outs = response.getOutputStream();
    outs.flush();
    outs.write(b,0,b.length);
    outs.close();
} catch(Throwable t) {
    t.printStackTrace();
}
%>
    
