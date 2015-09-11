class Chemical < ActiveRecord::Base
  self.table_name="chemicals"
  self.primary_key="name"
end
