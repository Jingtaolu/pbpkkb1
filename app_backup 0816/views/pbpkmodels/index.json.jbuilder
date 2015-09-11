json.array!(@pbpkmodels) do |pbpkmodel|
  json.extract! pbpkmodel, :id, :chemicalname
  json.url pbpkmodel_url(pbpkmodel, format: :json)
end
