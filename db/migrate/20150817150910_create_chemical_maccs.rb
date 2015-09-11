class CreateChemicalMaccs < ActiveRecord::Migration
  def change
    create_table :chemical_maccs do |t|

      t.timestamps null: false
    end
  end
end
