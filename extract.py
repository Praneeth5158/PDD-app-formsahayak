import glob, re
phrases = set()
for f in glob.glob('app/src/main/java/com/simats/formsahayak/ui/screens/*.kt'):
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()
        matches = re.findall(r'else\s*->\s*\"(.*?)\"', content)
        phrases.update(matches)

with open('phrases.txt', 'w', encoding='utf-8') as out:
    for p in sorted(phrases):
        out.write(p + '\n')
