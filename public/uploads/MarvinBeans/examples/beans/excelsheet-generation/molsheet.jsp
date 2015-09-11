<%--

    molsheet.jsp

    ChemAxon Ltd., 1999-2005

    Generates a MS Excel sheet with xml schemas
    
--%>
<!-- Including the required xml name spaces -->
<html xmlns:v='urn:schemas-microsoft-com:vml'
xmlns:o='urn:schemas-microsoft-com:office:office'>

<body>
<%@page import="java.net.URLEncoder"%>
<%
// Setting content type
response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-Disposition", "attachment; filename=moltable.xls");

// Setting molecules
String mol = java.net.URLEncoder.encode("CC1CC2(CC(=O)N(CCC3=C4=CC=CC=C4=CC=C3)C5CCCC25)C(C)C(C)=C1");
String mol2 = java.net.URLEncoder.encode("CC1=C2=CC3=C(NC4=C(Cl)C=CC(=C34)S(=O)(=O)C5=CC=C(Cl)C=C5)C=C2=CC=C1");

// Setting the molecules' image type
String format = java.net.URLEncoder.encode("jpeg:w150,h150");

%>
<!-- Creating a html table -->
<table border='1'>
  <tr>
    <th>Structure ID</th>
    <th>&nbsp;&nbsp;Structure Picture&nbsp;&nbsp;</th>
    <th>Formula</th>
    <th>Molweight</th>
  </tr>
  <tr valign="top">
    <td><br>1</td>
    <td width='160' height='160'>
	<!---->
	<v:shape href='' style='margin-left:1;margin-top:1;width:150%;height:150%'>
	<v:imagedata src='http://10.0.0.2:8081/cvs/marvin/examples/image-generation/generate_image.jsp?mol=<%=mol%>&format=<%=format%>' o:title=''/></v:shape>
    </td>
    <td><br>C28H35NO</td>
    <td><br>401.31</td>
  </tr>
  <tr valign="top">
    <td><br>2</td>
    <td width='160' height='160'>
	<v:shape href=''style='margin-left:1;margin-top:1;width:150%;height:150%'>
	<v:imagedata src='http://10.0.0.2:8081/cvs/marvin/examples/image-generation/generate_image.jsp?mol=<%=mol2%>&format=<%=format%>' o:title=''/></v:shape>
    </td>
    <td><br>C23H15C12NO2S</td>
    <td><br>440.22</td>
  </tr>
</table>
</body>
</html>

