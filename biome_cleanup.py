file=open("biomes.txt")
biomes = {}
overwrites = 0
for line in file.readlines():
    s = line.split(" = ")
    try:
        biomes[s[0]]
        overwrites += 1
    except:
        pass
    biomes[s[0]] = s[1]

print("Overwrites:",overwrites)
file.close()

file=open("biomes.txt","w")
for biome in biomes:
    file.write(biome)
    file.write(" = ")
    file.write(biomes[biome])

file.close()
input()
