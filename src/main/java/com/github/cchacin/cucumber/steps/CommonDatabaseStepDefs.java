/**
 * Copyright (C) 2014 Carlos Chacin (cchacin@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cchacin.cucumber.steps;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import gherkin.formatter.model.DataTableRow;
import gherkin.formatter.model.Row;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;

public class CommonDatabaseStepDefs {

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(CommonDatabaseStepDefs.class
                    .getResourceAsStream("/test_db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final Destination destination = new DriverManagerDestination(
            properties.getProperty("database.url"),
            properties.getProperty("database.user"),
            properties.getProperty("database.password"));

    @Given("^I have the following rows in the \"(.*?)\" table:$")
    public void i_have_the_following_rows_in_the_table(String tableName,
                                                       DataTable data) throws Throwable {
        this.insert(tableName, data);
    }

    @Given("^I have the only following rows in the \"([^\"]*)\" table:$")
    public void I_have_the_only_following_rows_in_the_table(
            final String tableName, final DataTable data) throws Throwable {
        this.deleteAll(tableName);
        this.insert(tableName, data);
    }

    private void insert(final String tableName, final DataTable data) {
        List<DataTableRow> rows = data.getGherkinRows();
        final List<String> columns = rows.get(0).getCells();

        final List<DataTableRow> rows2 = rows.subList(1, rows.size());

        List<Operation> operations = new ArrayList<>();
        Insert.Builder builder = Insert.into(tableName);
        builder.columns(columns.toArray(new String[columns.size()]));
        for (Row row : rows2) {
            builder.values(row.getCells().toArray(
                    new String[row.getCells().size()]));
            operations.add(builder.build());
        }

        this.apply(sequenceOf(operations));
    }

    private void deleteAll(final String tableName) {
        this.apply(deleteAllFrom(tableName));
    }

    private void apply(final Operation operation) {
        new DbSetup(destination, operation).launch();
    }
}
