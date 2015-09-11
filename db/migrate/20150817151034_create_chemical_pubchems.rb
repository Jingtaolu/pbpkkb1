class CreateChemicalPubchems < ActiveRecord::Migration
  def change
    create_table :chemical_pubchems do |t|

      t.timestamps null: false
    end
  end
end
