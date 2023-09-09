import numpy as np
import pandas as pd
import json
import tensorflow as tf
from flask import Flask, request
import librosa
import pickle

model = tf.keras.models.load_model("Mood_Eco.h5", compile=False)

classes = [
  'angry', 'calm', 'disgust', 'fear', 'happy', 'neutral', 'sad', 'surprise'
]

app = Flask(__name__)


# Load pickle file
def load_parameter(path):
  with open(f'{path}.pkl', 'rb') as file:
    return pickle.load(file)


def get_features(data):

  # duration and offset are used to take care of the no audio in start and the ending of each audio files as seen above.
  data, sample_rate = librosa.load(data, duration=2.5, offset=0.6)

  # ZCR
  result = np.array([])
  zcr = np.mean(librosa.feature.zero_crossing_rate(y=data).T, axis=0)
  result = np.hstack((result, zcr))  # stacking horizontally

  # Chroma_stft
  stft = np.abs(librosa.stft(data))
  chroma_stft = np.mean(librosa.feature.chroma_stft(S=stft, sr=sample_rate).T,
                        axis=0)
  result = np.hstack((result, chroma_stft))  # stacking horizontally

  # MFCC
  mfcc = np.mean(librosa.feature.mfcc(y=data, sr=sample_rate).T, axis=0)
  result = np.hstack((result, mfcc))  # stacking horizontally

  # Root Mean Square Value
  rms = np.mean(librosa.feature.rms(y=data).T, axis=0)
  result = np.hstack((result, rms))  # stacking horizontally

  # MelSpectogram
  mel = np.mean(librosa.feature.melspectrogram(y=data, sr=sample_rate).T,
                axis=0)
  result = np.hstack((result, mel))  # stacking horizontally

  return np.array(result)


def getClass(X):
  Features = pd.DataFrame(X)
  X = Features.iloc[:, :].values

  scaler = load_parameter('scaler')
  X = scaler.transform(X.T)
  X = np.expand_dims(X, axis=2)

  pred_test = model.predict(X)


  index = np.argmax(pred_test[0])
  voice_class = classes[index]

  return voice_class


@app.route('/Check', methods=["POST"])
def index():
  if 'voice_message' in request.files:
    file = request.files['voice_message']
    features = get_features(file)
    voice_class = getClass(features)
    data = {'voice class': f'{voice_class}'}
    json_data = json.dumps(data)
    return json_data
  data = {'voice class': 'voice sound not found'}
  json_data = json.dumps(data)
  return json_data


if __name__ == "__main__":
  app.run("0.0.0.0", debug=False, port=8000)
