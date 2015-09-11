' config.vbs
'
' VBScript to configure environment for batch files of Marvin Beans package
' @author Tamas Vertse
' @version 04/04/2006

' Determines MARVINBEANSHOME
Function getMarvinBeansHome()

    scriptfullname = WScript.ScriptFullName
    scriptname = "\bin\" & WScript.ScriptName
    index = InStr(1, scriptfullname, scriptname, 0)
    mbhome = Left(scriptfullname, index-1)
    getMarvinBeansHome = mbhome
End Function

' Determines the location of the JRE
' Returns the installer used JRE or null
Function getJre(installdir)
    Dim fso, f
    jre = nul
    ' tries the installer bundled JRE
    jreconfigfile = installdir & "\.install4j\pref_jre.cfg"
    Set fso = CreateObject("Scripting.FileSystemObject")
    If(fso.FileExists(jreconfigfile)) Then
        Set f = fso.openTextFile(jreconfigfile)
        jre = f.readLine() & "\bin\java.exe"
        If(fso.FileExists(jre)) Then
            getJre = jre
            Exit Function
        End If
        WScript.Echo jre & " does not exist"
    End If

    ' tries the installer used JRE
    jreconfigfile = installdir & "\.install4j\inst_jre.cfg"
    If(fso.FileExists(jreconfigfile)) Then
        Set f = fso.openTextFile(jreconfigfile)
        jre = f.readLine() & "\bin\java.exe"
        If(fso.FileExists(jre)) Then
            getJre = jre
            Exit Function
        End If
        WScript.Echo jre & " does not exist"
    End If
    getJre = nul
End Function

'Read text file
function GetFile(FileName)
  If FileName<>"" Then
    Dim FS, FileStream
    Set FS = CreateObject("Scripting.FileSystemObject")
      on error resume Next
      Set FileStream = FS.OpenTextFile(FileName)
      GetFile = FileStream.ReadAll()
      FS.Close()
  End If
End Function

'Write string As a text file.
function WriteFile(FileName, Contents)
  Dim OutStream, FS

  on error resume Next
  Set FS = CreateObject("Scripting.FileSystemObject")
    Set OutStream = FS.OpenTextFile(FileName, 2, True)
    OutStream.Write Contents
    FS.Close()
End Function

' Replaces the first occurance of the pattern to the new value in the
' specified file
Sub updateFile(filename,  find, replacewith)
    Dim fso, f
    Set fso = CreateObject("Scripting.FileSystemObject")
    If(fso.FileExists(filename)) Then
        ' read the content of the file
        filecontent = GetFile(filename)
        newcontent = replace(filecontent, find, replacewith, 1, 1, 1)
        If newcontent <> filecontent Then
           ' write result if different
           WScript.Echo "Overwrite " & filename
           WriteFile filename,newcontent
        End If
    End If
End Sub

' determines Marvin Beans home and Java home
WScript.Echo "Determines variables for batch files and update it it is needed."
marvinbeanshome = getMarvinBeansHome()
WScript.Echo "MARVINBEANSHOME=" & marvinbeanshome
javapath = getJre(marvinbeanshome)

' update Java path in bin/setup.bat
If(javapath <> nul) Then
    WScript.Echo "JVMPATH=" & javapath
    batchfile = marvinbeanshome & "\bin\setup.bat"
    find = "@JVMPATH@"
    replacewith = javapath
    updateFile batchfile, find, replacewith
End If

' update MARVINBEANSHOME in bin/*.bat
find = "SET MARVINBEANSHOME=.."
replacewith = "SET MARVINBEANSHOME=" & marvinbeanshome
dirname = marvinbeanshome & "\bin"
Dim fso, d , files
Set fso = CreateObject("Scripting.FileSystemObject")
If(fso.FolderExists(dirname)) Then
    Set d = fso.GetFolder(dirname)
    Set files = d.Files
    For Each file In files
        If(Right(file.Path,Len(".bat")) = ".bat") Then
           updateFile file.Path,find,replacewith
        End If
   Next
End If

WScript.Quit
