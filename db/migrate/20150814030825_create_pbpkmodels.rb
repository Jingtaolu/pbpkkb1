class CreatePbpkmodels < ActiveRecord::Migration
  def change
    create_table :pbpkmodels do |t|
      t.string :chemicalname

      t.timestamps null: false
    end
  end
end
