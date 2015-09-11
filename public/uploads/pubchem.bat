cd public\uploads
copy /y user_uploaded fpcalc\pubchem\user_uploaded
cd fpcalc\pubchem
java -jar PaDEL-Descriptor.jar -fingerprints -dir user_uploaded -file pubchem_fingerprint.txt
cd..
cd..
copy /y fpcalc\pubchem\pubchem_fingerprint.txt pubchem_fingerprint.txt
