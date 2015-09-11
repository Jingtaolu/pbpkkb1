require 'test_helper'

class PbpkmodelsControllerTest < ActionController::TestCase
  setup do
    @pbpkmodel = pbpkmodels(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:pbpkmodels)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create pbpkmodel" do
    assert_difference('Pbpkmodel.count') do
      post :create, pbpkmodel: { chemicalname: @pbpkmodel.chemicalname }
    end

    assert_redirected_to pbpkmodel_path(assigns(:pbpkmodel))
  end

  test "should show pbpkmodel" do
    get :show, id: @pbpkmodel
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @pbpkmodel
    assert_response :success
  end

  test "should update pbpkmodel" do
    patch :update, id: @pbpkmodel, pbpkmodel: { chemicalname: @pbpkmodel.chemicalname }
    assert_redirected_to pbpkmodel_path(assigns(:pbpkmodel))
  end

  test "should destroy pbpkmodel" do
    assert_difference('Pbpkmodel.count', -1) do
      delete :destroy, id: @pbpkmodel
    end

    assert_redirected_to pbpkmodels_path
  end
end
