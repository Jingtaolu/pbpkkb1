cd public\uploads
copy /y user_uploaded fpcalc\maccs\user_uploaded
cd fpcalc\maccs
java -jar PaDEL-Descriptor.jar -fingerprints -dir user_uploaded -file maccs_fingerprint.txt
cd..
cd..
copy /y fpcalc\maccs\maccs_fingerprint.txt maccs_fingerprint.txt
