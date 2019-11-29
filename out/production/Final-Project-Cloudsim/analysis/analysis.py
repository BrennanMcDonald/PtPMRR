import json
import matplotlib
import matplotlib.pyplot as plt
import numpy as np

seperator = "==================================="
ptpmrr = "PTPMRR"
rr = "RR"
sjf = "SJF"
fcfs = "FCFS"

f = open("output.log")
runs = []
sums = {}

sums[ptpmrr] = {
    'cost': 0,
    'cpu': 0,
    'makespan': 0
}

sums[rr] = {
    'cost': 0,
    'cpu': 0,
    'makespan': 0
}

sums[sjf] = {
    'cost': 0,
    'cpu': 0,
    'makespan': 0
}

sums[fcfs] = {
    'cost': 0,
    'cpu': 0,
    'makespan': 0
}
line = f.readline()
while (line != None and line != ""):
    obj = {}
    obj[ptpmrr] = []
    for a in range (3):
        line = f.readline()
        obj[ptpmrr].append(line.split(" ")[len(line.split(" "))-1].replace('\n',''))
    obj[fcfs] = []
    for a in range (3):
        line = f.readline()
        obj[fcfs].append(line.split(" ")[len(line.split(" "))-1].replace('\n',''))
    obj[rr] = []
    for a in range (3):
        line = f.readline()
        obj[rr].append(line.split(" ")[len(line.split(" "))-1].replace('\n',''))
    obj[sjf] = []
    for a in range (3):
        line = f.readline()
        obj[sjf].append(line.split(" ")[len(line.split(" "))-1].replace('\n',''))
    runs.append(obj)
    line = f.readline()
    line = f.readline()

f = open("runs.json", "w")

f.write(json.dumps(runs))

for run in runs:
    for find in (ptpmrr, rr, sjf, fcfs):
        a,b,c = (float(run[find][0]), float(run[find][1]), float(run[find][2]))
        sums[find]['makespan'] += a
        sums[find]['cost'] += b
        sums[find]['cpu'] += c

for find in (ptpmrr, rr, sjf, fcfs):
    sums[find]['avg_makespan'] = sums[find]['makespan']/len(runs);
    sums[find]['avg_cost'] = sums[find]['cost']/len(runs);
    sums[find]['avg_cpu'] = sums[find]['cpu']/len(runs);


labels = [ptpmrr, rr, sjf, fcfs]
ptpmrr_vals = list(sums[ptpmrr].values())
sjf_vals = list(sums[sjf].values())

avg_makespans = list(map(lambda x: sums[x]['avg_makespan'], sums))
avg_costs = list(map(lambda x: sums[x]['avg_cost'], sums))
avg_cpus = list(map(lambda x: sums[x]['avg_cpu'], sums))
        
x = np.arange(len(labels))  # the label locations
width = 0.35  # the width of the bars

fig, ax = plt.subplots()
rects5 = ax.bar(x + width/6, avg_makespans, width, label='avg_makespans')

# Add some text for labels, title and custom x-axis tick labels, etc.
ax.set_ylabel('Averages')
ax.set_title('Average Makespans of 100 000 runs to run 30 tasks')
ax.set_xticks(x)
ax.set_xticklabels(labels)
ax.legend()


def autolabel(rects):
    """Attach a text label above each bar in *rects*, displaying its height."""
    for rect in rects:
        height = rect.get_height()
        ax.annotate('{}'.format(height),
                    xy=(rect.get_x() + rect.get_width() / 2, height),
                    xytext=(0, 3),  # 3 points vertical offset
                    textcoords="offset points",
                    ha='center', va='bottom')


autolabel(rects5)

fig.tight_layout()

plt.show()