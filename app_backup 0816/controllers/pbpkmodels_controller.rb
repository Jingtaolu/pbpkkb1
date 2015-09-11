class PbpkmodelsController < ApplicationController
  before_action :set_pbpkmodel, only: [:show, :edit, :update, :destroy]

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
