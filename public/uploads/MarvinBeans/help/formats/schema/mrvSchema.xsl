<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" version="1.0"
    xmlns:cxn="http://www.chemaxon.com">
	<!-- Main template for selecting the elements under the root -->
 <xsl:strip-space elements="*"/>         

 <xsl:template match="/">
 <html>
  <head>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
   <meta NAME="description" CONTENT="mrv file format"/>
   <meta NAME="keywords" CONTENT="mrv, schema, marvin, file format"/>
   <meta NAME="author" CONTENT="Peter Szakacs"/>
  </head>

  <body>
    <p>
    Codename: <strong>mrv</strong>
    </p>
    <p>
    An mrv file may contain four elements,
    <a href="#cml">cml</a>, 
    <a href="#MDocument">MDocument</a>, 
    <a href="#molecule">molecule</a>, 
    <a href="#reaction">reaction</a>,
    all of which has complex type.
    The elements of the complex types are shown as list items,
    while its attributes are written with italic under the name of
    the corresponding complex type name.
    </p>
     <xsl:for-each select="xs:schema/xs:element">
      <xsl:variable name="ttype" select="substring-after(@type, 'cxn:')" />
      <xsl:variable name="nname" select="@name" />
       <h2>
       <a name="{@name}"></a> 
        Element: <xsl:value-of select="@name"></xsl:value-of>
       </h2>
       <xsl:call-template name="PrintComplexSubtypes">
        <xsl:with-param name="type" select="substring-after(@type, 'cxn:')" />
        <xsl:with-param name="indent" select="1" />
       </xsl:call-template>
      </xsl:for-each>
   <h2> <a name="escape"></a>Escape characters</h2>
    Special value of an element or attribute is escaped as follows:<ul>
    <table>
     <tr>
      <td> null </td>
      <td> "0"</td>
     </tr>
     <tr>
      <td> 0 </td>
      <td> "zero" <i>(character string)</i></td>
     </tr>
     <tr>
      <td> no value   </td>
      <td> "."</td>
     </tr>
     <tr>
      <td> "."</td>
      <td> &#38;#<i>n</i>;, <i>n</i> is the character code  </td>
     </tr>
    </table>
   </ul>

    
   </body>
  </html>
 </xsl:template>
 
 
 <xsl:template match="xs:annotation/xs:documentation">
  <xsl:value-of select="."></xsl:value-of>
 </xsl:template>
 
 <xsl:template match="xs:annotation/xs:documentation">
  <xsl:value-of select="."></xsl:value-of>
 </xsl:template>
 
 <xsl:template name="indent">
    <xsl:param name="count" />
    <xsl:param name="i" />
    <xsl:if test="$i &lt;= $count">
     <xsl:call-template name="indent">
       <xsl:with-param name="i" select="$i + 1 " />
       <xsl:with-param name="count" select="$count" />
     </xsl:call-template>
    </xsl:if>
 </xsl:template>
 
 <xsl:template name="PrintComplexSubtypes">
  <xsl:param name="type" />
  <xsl:param name="indent" />
  <xsl:param name="isCallingType">true</xsl:param>
  <xsl:param name="typeList" />
  <xsl:variable name="typeName" select="@name" />
 		<!-- Print attributes for complex type -->
  <table>
    <xsl:for-each select="//xs:complexType[@name=$type]//xs:attribute"> 
     <tr><td><i><xsl:value-of select="@name"></xsl:value-of></i></td>
     <td><xsl:apply-templates select="."/></td></tr>
    </xsl:for-each>
  </table>
  <xsl:for-each select="//xs:complexType[@name=$type]//xs:element">
     <xsl:choose>
      <xsl:when test="substring-after(@type,'cxn:') = $type">
      </xsl:when>
      <xsl:otherwise>
       <ul>
        <li>
         <xsl:variable name="subType" select="@type" />
         <xsl:choose>
          <xsl:when test="@name='MDocument'">  
            <a href="#{@name}">
            <xsl:value-of select="@name"></xsl:value-of></a>:
            <xsl:apply-templates select="." />
          </xsl:when>
          <xsl:when test="@name='molecule' and substring-after(@type,'cxn:')='moleculeType'">  
            <a href="#{@name}">
            <xsl:value-of select="@name"></xsl:value-of></a>:
            <xsl:apply-templates select="." />
          </xsl:when>
          <xsl:when test="@name='reaction'">  
            <a href="#{@name}">
            <xsl:value-of select="@name"></xsl:value-of></a>:
            <xsl:apply-templates select="." />
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="@name"></xsl:value-of>:
            <xsl:apply-templates select="." /> 
            <xsl:call-template name="PrintComplexSubtypes">
             <xsl:with-param name="type" select="substring-after($subType, 'cxn:')" />
             <xsl:with-param name="indent" select="$indent +1" />
             <xsl:with-param name="isCallingType">false</xsl:with-param>
             <xsl:with-param name="typeList"
              select="concat($typeList, '*', $typeName, '+')" />
           </xsl:call-template>
          </xsl:otherwise>
         </xsl:choose>
        </li>
       </ul>
      </xsl:otherwise>
     </xsl:choose>
  </xsl:for-each>
 </xsl:template>
 
</xsl:stylesheet>
 
