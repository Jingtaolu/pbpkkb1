class FileupController < ApplicationController

  def upload
    @file1=ChemicalMacc.new
  end

  def display

    # write file to disk
    uploaded_io = params[:chemical_macc][:something]
    File.open(Rails.root.join('public', 'uploads', 'user_uploaded'), 'wb') do |file|
      file.write(uploaded_io.read)
    end

    # convert mol file to picture
    %x(bash public/uploads/MarvinBeans/bin/molconvert jpeg:w500,h500 public/uploads/user_uploaded -o public/uploads/chemstruct.jpeg)

    # read mol file and read to variable (string)
  mol=File.open(Rails.root.join('public', 'uploads', 'user_uploaded'), 'rb') do |file|
    file.read
  end

    # covert string variable (the mol file) to array of strings
    mol_a=mol.split("\n")
    start=mol_a.index{|a| a.include? ("M  END")}+1
    @disp=mol_a[start...-1].map{|a| a.gsub(/> /, "")}


    pchem= mol_a.map {|a| a.include? ("PUBCHEM_COMPOUND_CID")}
    if pchem.include? true
    Rails.cache.write("PUBCHEM_COMPOUND_CID",mol_a[pchem.index(true)+1])
    Rails.cache.write("CSID",nil)
    end

    chsp=mol_a.map {|a| a.include? ("CSID")}
    if chsp.include? true
      Rails.cache.write("CSID",mol_a[chsp.index(true)+1])
      Rails.cache.write("PUBCHEM_COMPOUND_CID",nil)
    end

    @PUBCHEM_COMPOUND_CID=Rails.cache.read("PUBCHEM_COMPOUND_CID")
    @CSID = Rails.cache.read("CSID")

  end


  def maccs_keys
    @PUBCHEM_COMPOUND_CID=Rails.cache.read("PUBCHEM_COMPOUND_CID")
    @CSID = Rails.cache.read("CSID")
    %x(java -jar public/uploads/fpcalc/maccs/PaDEL-Descriptor.jar -fingerprints -dir public/uploads/user_uploaded -file public/uploads/maccs_fingerprint.txt)

    f=IO.readlines("public/uploads/maccs_fingerprint.txt")
    @f0=f[0].split(",")
    @f1=f[1].split(",")

  end

  def pubchem_keys
    @PUBCHEM_COMPOUND_CID=Rails.cache.read("PUBCHEM_COMPOUND_CID")
    @CSID = Rails.cache.read("CSID")
    %x(java -jar public/uploads/fpcalc/pubchem/PaDEL-Descriptor.jar -fingerprints -dir public/uploads/user_uploaded -file public/uploads/pubchem_fingerprint.txt)

    f=IO.readlines("public/uploads/pubchem_fingerprint.txt")
    @f0=f[0].split(",")
    @f1=f[1].split(",")

  end



  def maccs_tanimoto
    @PUBCHEM_COMPOUND_CID=Rails.cache.read("PUBCHEM_COMPOUND_CID")
    @CSID = Rails.cache.read("CSID")
    #ttt.each will change the class from active record to ruby array
    # ttt=Chemical1.where(pubchemfp1:'1')
    #ttt1=ttt.each { |aaa| aaa.name=aaa.name+"contr" } #ttt.each will change the class from active record to ruby array
    #@iii = ttt1.sort_by {|x| x.name}
    #
    #as_json will change  active record to hash
    chems=ChemicalMacc.all
    chems_a_h=chems.as_json
    chems_a_a=chems_a_h.map(&:values)
      chems_a_a_int=chems_a_a.map do |a|
      a.delete_at(0)
      a=a.map {|b| b.strip.to_i}
    end

    fp=IO.readlines("public/uploads/maccs_fingerprint.txt")
    fp1=fp[1].split(",")
    fp1.delete_at(0)
    fp1=fp1.map {|b| b.strip.to_i}


    tani_idx=chems_a_a_int.map do |a|
    a_a=a.zip(a).map { |x,y| x*y }
    fp_fp=fp1.zip(fp1).map { |x,y| x*y }
    a_fp=a.zip(fp1).map { |x,y| x*y }
    sum_a_a=a_a.inject(:+)
    sum_fp_fp=fp_fp.inject(:+)
    sum_a_fp=a_fp.inject(:+)
    a=sum_a_fp.to_f / (sum_a_a.to_f + sum_fp_fp.to_f - sum_a_fp.to_f)
    end

    chems_a_a2=chems_a_h.map(&:values)
   name= chems_a_a2.map {|a| a=a[0].gsub('"', '')}
    h = Hash[name.zip tani_idx]
    h=h.sort_by {|key, value| value}


    @tanimoto_rank=h.reverse

    #pass array to different controller action
    Rails.cache.write("array",h.reverse)

  end



  def pubchem_tanimoto
    @PUBCHEM_COMPOUND_CID=Rails.cache.read("PUBCHEM_COMPOUND_CID")
    @CSID = Rails.cache.read("CSID")
    #ttt.each will change the class from active record to ruby array
    # ttt=Chemical1.where(pubchemfp1:'1')
    #ttt1=ttt.each { |aaa| aaa.name=aaa.name+"contr" } #ttt.each will change the class from active record to ruby array
    #@iii = ttt1.sort_by {|x| x.name}
    #
    #as_json will change  active record to hash
    chems=ChemicalPubchem.all
    chems_a_h=chems.as_json
    chems_a_a=chems_a_h.map(&:values)
    chems_a_a_int=chems_a_a.map do |a|
      a.delete_at(0)
      a=a.map {|b| b.strip.to_i}
    end

    fp=IO.readlines("public/uploads/pubchem_fingerprint.txt")
    fp1=fp[1].split(",")
    fp1.delete_at(0)
    fp1=fp1.map {|b| b.strip.to_i}


    tani_idx=chems_a_a_int.map do |a|
      a_a=a.zip(a).map { |x,y| x*y }
      fp_fp=fp1.zip(fp1).map { |x,y| x*y }
      a_fp=a.zip(fp1).map { |x,y| x*y }
      sum_a_a=a_a.inject(:+)
      sum_fp_fp=fp_fp.inject(:+)
      sum_a_fp=a_fp.inject(:+)
      a=sum_a_fp.to_f / (sum_a_a.to_f + sum_fp_fp.to_f - sum_a_fp.to_f)
    end

    chems_a_a2=chems_a_h.map(&:values)
    name= chems_a_a2.map {|a| a=a[0].gsub('"', '')}
    h = Hash[name.zip tani_idx]
    h=h.sort_by {|_key, value| value}




    @tanimoto_rank=h.reverse

    #pass array to different controller action
    Rails.cache.write("array",h.reverse)
  end

  end


