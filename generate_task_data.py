import pandas as pd
import random

num_tasks = 300  # enough for Random Forest

data = []
for i in range(num_tasks):
    input_size = random.randint(50, 200)      # size of task
    op_type = random.choice(['CPU', 'IO', 'Mixed'])
    data.append([i, input_size, op_type])

df = pd.DataFrame(data, columns=['task_id', 'input_size', 'op_type'])
df.to_csv('data/task_data.csv', index=False)
print("task_data.csv generated with 300 tasks")
