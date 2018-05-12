all:

setup:
	graql console -k ast_grakn -f ./schema.gql
	graql console -k ast_grakn -f ./sample-data.gql

query:
	graql console -k ast_grakn -e 'match $$x isa compileunit;$$x has name $$xn;$$y has name $$yn;(cu_source:$$x,cu_dependency:$$y) isa depend_compileunit;get $$xn,$$yn;'


clean-grakn:
	graql console -k ast_grakn -e 'clean'
