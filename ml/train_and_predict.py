import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error

# 1️⃣ Load actual runtimes (from Day 1)
df = pd.read_csv('data/task_actual_runtimes.csv')

# Features and label
X = df[['input_size', 'op_type']]
y = df['actual_runtime_ms']

# One-hot encode op_type
encoder = OneHotEncoder(sparse_output=False)
op_type_encoded = encoder.fit_transform(X[['op_type']])
op_type_df = pd.DataFrame(op_type_encoded, columns=encoder.get_feature_names_out(['op_type']))

X_encoded = pd.concat([X[['input_size']].reset_index(drop=True), op_type_df], axis=1)

# Train/test split
X_train, X_test, y_train, y_test = train_test_split(X_encoded, y, test_size=0.2, random_state=42)

# Train Random Forest
rf = RandomForestRegressor(n_estimators=100, random_state=42)
rf.fit(X_train, y_train)

# Evaluate
y_pred = rf.predict(X_test)
mse = mean_squared_error(y_test, y_pred)
print(f"Test MSE: {mse:.2f}")

# 2️⃣ Predict runtimes for all tasks (features only)
df_tasks = pd.read_csv('data/task_data.csv')

# Encode op_type
op_type_encoded_tasks = encoder.transform(df_tasks[['op_type']])
op_type_df_tasks = pd.DataFrame(op_type_encoded_tasks, columns=encoder.get_feature_names_out(['op_type']))
X_tasks_encoded = pd.concat([df_tasks[['input_size']].reset_index(drop=True), op_type_df_tasks], axis=1)

# Predict
df_tasks['predicted_runtime_ms'] = rf.predict(X_tasks_encoded)

# Save predictions
df_tasks.to_csv('data/task_predicted_runtimes.csv', index=False)
print("Predicted runtimes saved to data/task_predicted_runtimes.csv")
