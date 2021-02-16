# react-native-thub-rn-fingerprint

React Native module for Fingerprint

## Installation

```sh
npm install react-native-thub-rn-fingerprint
```

## Usage

```js
import ThubRnFingerprint from 'react-native-thub-rn-fingerprint';

ThubRnFingerprint.authenticateLocally(
  (data) => {
    Alert.alert(data);
  },
  (errorMessage) => {
    Alert.alert(errorMessage);
  }
);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
