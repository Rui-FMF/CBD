from neo4j import GraphDatabase


class PokemonGraphController(object):

    def __init__(self, uri, user, password):
        self._driver = GraphDatabase.driver(uri, auth=(user, password))

    def close(self):
        self._driver.close()

    def insert_data(self):
        self.insert_nodes()
        self.insert_relations()

    def insert_nodes(self):
        with self._driver.session() as session:
            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MERGE (player:Player {name: Row.Player}) SET  player.number=Row.`Squad Number`, player.position=Row.Position, player.birthday=Row.`Date of Birth`, player.age=Row.Age, player.caps=Row.Caps, player.goals=Row.Goals")

            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MERGE (country:Country {name: Row.Team}) SET  country.group=Row.Group")

            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MERGE (club:Club {name: Row.Club})")

    def insert_relations(self):
        with self._driver.session() as session:
            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MATCH(player:Player {name: Row.Player}),(country:Country {name: Row.Team}) CREATE (player)-[:From]->(country)")

            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MATCH(player:Player {name: Row.Player}),(club:Club {name: Row.Club}) CREATE (player)-[:Plays_At]->(club)")

    def run_query(self, query):
        with self._driver.session() as session:
            return list(session.run(query))


if __name__ == "__main__":
    controller = PokemonGraphController(
        "bolt://localhost:7687", "neo4j", "ronaldo")

    #controller.insert_data()

    counter = 1

    query_list = ["MATCH (country:Country) RETURN country.name",
                    "MATCH (p: Player) WHERE toInteger(p.goals) > 50 RETURN p.name, p.goals",
                    "MATCH (p: Player) RETURN p.name, p.caps ORDER BY toInteger(p.caps) DESC LIMIT 4",
                    "MATCH (p: Player) WHERE(p.name STARTS WITH 'Sergio') RETURN p.name",
                    "MATCH (country:Country {group : 'D'}) RETURN country.name, country.group",
                    "MATCH (p: Player {position : 'MF'} ) WHERE toInteger(p.age) > 34 RETURN p",
                    "MATCH (p: Player {position : 'DF'})-[:From]->(c:Country {name : 'Portugal'}) RETURN p.name, p.position, c.name",
                    "MATCH (p: Player)-[:Plays_At]->(c:Club {name : 'Benfica'}) RETURN p.name, c.name",
                    "MATCH (p: Player {position : 'FW'})-[:From]->(c:Country {group : 'G'}) WHERE toInteger(p.age) < 22 RETURN p.name, c.group, p.position, p.age",
                    "MATCH (p: Player)-[:From]->(c:Country {group : 'B'}) MATCH (p: Player)-[:Plays_At]->(cl:Club {name : 'Manchester City'})  RETURN p.name, c.name, c.group, cl.name"]
    
    titles = ["Get all the Teams that participated in the 2018 World Cup",
                "Get the players with over 50 international goals",
                "Get the 4 players with the most appearances",
                "Get all players named Sergio",
                "Get all the teams in group D",
                "Get all midfielders over the age of 34",
                "Get all portuguese defenders",
                "Get all World cup players that play at Benfica",
                "Get all forwards in Group G younger than 22",
                "Get all players in Group B that play at Manchester City"]
    with open("output.txt", "w") as writer:
        for current_query in query_list:
            query = current_query
            writer.write("Query " +  str(counter) + ": " + titles[counter-1]+"\n")
            writer.write("  " +  current_query +"\n")

            counter += 1

            query_result = controller.run_query(query)
            for i in query_result:
                s=""
                for j in range(len(i.items())):
                    s += str(i.items()[j][1]) + ", "
                writer.write("  " + str(s) + "\n")
            writer.write("\n\n")

    controller.close()