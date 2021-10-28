dic = {}

with open("female-names.txt", "r") as fp:
    for line in fp:

        if line[0].upper() not in dic:
            dic[line[0].upper()] = 0

        dic[line[0].upper()] += 1


with open("initials4redis.txt", 'w') as fw:
    for init, num in dic.items():
        fw.write(f' SET "{init}" "{num}"\n')