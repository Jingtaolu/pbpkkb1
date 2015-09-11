class FileupController < ApplicationController

  def upload
    @file1=Chemical.new
  end

  def display
    uploaded_io = params[:chemical][:something]
    File.open(Rails.root.join('public', 'uploads', 'user_uploaded'), 'wb') do |file|
      file.write(uploaded_io.read)
    end
    %x(public\\uploads\\MarvinBeans\\bin\\molconvert jpeg:w500,h500 public\\uploads\\user_uploaded -o public\\uploads\\chemstruct.jpeg)



  end


  def maccs_key

    %x(public\\uploads\\MarvinBeans\\bin\\molconvert jpeg:w500,h500 public\\uploads\\user_uploaded -o public\\uploads\\chemstruct.jpeg)

    %x(java -jar public/uploads/PaDEL/maccs_keys/PaDEL-Descriptor.jar -fingerprints -dir public/uploads -file public/uploads/maccs_fingerprint.txt)
    %x(java -jar public/uploads/PaDEL/pubchem_keys/PaDEL-Descriptor.jar -fingerprints -dir public/uploads -file public/uploads/pubchem_fingerprint.txt)

    #@fp=%x(type public\\uploads\\fingerprint.txt)

    f=IO.readlines("public\\uploads\\fingerprint.txt")
    @f0=f[0].split(",")
    @f1=f[1].split(",")

  end

  def pubchem_key
    uploaded_io = params[:chemical][:something]
    File.open(Rails.root.join('public', 'uploads', 'user_uploaded'), 'wb') do |file|
      file.write(uploaded_io.read)
    end
    %x(public\\uploads\\MarvinBeans\\bin\\molconvert jpeg:w500,h500 public\\uploads\\user_uploaded -o public\\uploads\\chemstruct.jpeg)

    %x(java -jar public/uploads/PaDEL/maccs_keys/PaDEL-Descriptor.jar -fingerprints -dir public/uploads -file public/uploads/maccs_fingerprint.txt)
    %x(java -jar public/uploads/PaDEL/pubchem_keys/PaDEL-Descriptor.jar -fingerprints -dir public/uploads -file public/uploads/pubchem_fingerprint.txt)

    #@fp=%x(type public\\uploads\\fingerprint.txt)

    f=IO.readlines("public\\uploads\\fingerprint.txt")
    @f0=f[0].split(",")
    @f1=f[1].split(",")

  end







  def tanimoto


    #ttt.each will change the class from active record to ruby array
    # ttt=Chemical1.where(pubchemfp1:'1')
    #ttt1=ttt.each { |aaa| aaa.name=aaa.name+"contr" } #ttt.each will change the class from active record to ruby array
    #@iii = ttt1.sort_by {|x| x.name}
    #
    #as_json will change  active record to hash
    chems=Chemical.all
    chems_a_h=chems.as_json
    chems_a_a=chems_a_h.map(&:values)
      chems_a_a_int=chems_a_a.map do |a|
      a.delete_at(0)
      a=a.map {|b| b.strip.to_i}
    end

    fp=IO.readlines("public\\uploads\\fingerprint.txt")
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




    @f11=h


  end
  end


