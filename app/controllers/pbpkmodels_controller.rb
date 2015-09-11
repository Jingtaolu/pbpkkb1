class PbpkmodelsController < ApplicationController
  before_action :set_pbpkmodel, only: [:show, :edit, :update, :destroy]

  def show_models
    @PUBCHEM_COMPOUND_CID=Rails.cache.read("PUBCHEM_COMPOUND_CID")
    @CSID = Rails.cache.read("CSID")

    @tanimoto_rank = Rails.cache.read("array")
    ranked_chemical_a_a=@tanimoto_rank.map {|a| a=a[0]}
    ranked_chemical=ranked_chemical_a_a.flatten

    pbpkmodels_active_record = Pbpkmodel.all
    pbpkmodels_a_h=pbpkmodels_active_record.as_json

    pbpkmodels_a_h_calc_rank=pbpkmodels_a_h.each {|a| a[:similarity_ranking]=ranked_chemical.index(a["chemical"])}
    @pbpkmodels_all=pbpkmodels_a_h_calc_rank
    #pbpkmodels_notnil=pbpkmodels_a_h_calc_rank.reject {|a| a[:similarity_ranking].nil?}
    #@pbpkmodels_a_h_ordered=pbpkmodels_notnil.sort_by { |a| a[:similarity_ranking]}
    @pbpkmodels_chemical=pbpkmodels_a_h_calc_rank.map do |a|
      a.select {|k,v| k=="chemical" }["chemical"]
    end
  end


  def search_models
    @PUBCHEM_COMPOUND_CID=Rails.cache.read("PUBCHEM_COMPOUND_CID")
    @CSID = Rails.cache.read("CSID")

    @tanimoto_rank = Rails.cache.read("array")
    ranked_chemical_a_a=@tanimoto_rank.map {|a| a=a[0]}
    ranked_chemical=ranked_chemical_a_a.flatten

    pbpkmodels_active_record = Pbpkmodel.all
    pbpkmodels_a_h=pbpkmodels_active_record.as_json

  #  pbpkmodels_a_h_calc_rank=pbpkmodels_a_h.each {|a| a[:similarity_ranking]=ranked_chemical.index(a["chemical"])}
    @pbpkmodels_searched=pbpkmodels_a_h.select do |b|
      c=b.map {|k,v| v.include? params["q"]}
      c.include? true
    end

    @pbpk_searched_models_chemical=@pbpkmodels_searched.map do |a|
      a.select {|k,v| k=="chemical" }["chemical"]
    end
  end










  # GET /pbpkmodels
  # GET /pbpkmodels.json
  def index
    @pbpkmodels = Pbpkmodel.all
  end

  # GET /pbpkmodels/1
  # GET /pbpkmodels/1.json
  def show
  end

  # GET /pbpkmodels/new
  def new
    @pbpkmodel = Pbpkmodel.new
  end

  # GET /pbpkmodels/1/edit
  def edit
  end

  # POST /pbpkmodels
  # POST /pbpkmodels.json
  def create
    @pbpkmodel = Pbpkmodel.new(pbpkmodel_params)

    respond_to do |format|
      if @pbpkmodel.save
        format.html { redirect_to @pbpkmodel, notice: 'Pbpkmodel was successfully created.' }
        format.json { render :show, status: :created, location: @pbpkmodel }
      else
        format.html { render :new }
        format.json { render json: @pbpkmodel.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /pbpkmodels/1
  # PATCH/PUT /pbpkmodels/1.json
  def update
    respond_to do |format|
      if @pbpkmodel.update(pbpkmodel_params)
        format.html { redirect_to @pbpkmodel, notice: 'Pbpkmodel was successfully updated.' }
        format.json { render :show, status: :ok, location: @pbpkmodel }
      else
        format.html { render :edit }
        format.json { render json: @pbpkmodel.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /pbpkmodels/1
  # DELETE /pbpkmodels/1.json
  def destroy
    @pbpkmodel.destroy
    respond_to do |format|
      format.html { redirect_to pbpkmodels_url, notice: 'Pbpkmodel was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_pbpkmodel
      @pbpkmodel = Pbpkmodel.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def pbpkmodel_params
      params.require(:pbpkmodel).permit(:chemicalname)
    end
end
