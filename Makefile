all:

setup:
	graql console -k ast_grakn -f ./schema.gql

sampledata:
	graql console -k ast_grakn -f ./sample-data.gql

query:
	graql console -k ast_grakn -e 'match $$x isa compileunit;$$x has name $$xn;$$y has name $$yn;($$x,$$y) isa depend_compileunit;get $$xn,$$yn;'
