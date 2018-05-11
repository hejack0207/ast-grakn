all:

setup:
	graql console -k ast_grakn -f ./schema.gql

sampledata:
	graql console -k ast_grakn -f ./sample-data.gql

