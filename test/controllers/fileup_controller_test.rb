require 'test_helper'

class FileupControllerTest < ActionController::TestCase
  test "should get get" do
    get :get
    assert_response :success
  end

  test "should get save" do
    get :save
    assert_response :success
  end

  test "should get tanimoto" do
    get :tanimoto
    assert_response :success
  end

end
