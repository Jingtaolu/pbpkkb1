json.array!(@chemicals) do |chemical|
  json.extract! chemical, :id, :name
  json.url chemical_url(chemical, format: :json)
end
