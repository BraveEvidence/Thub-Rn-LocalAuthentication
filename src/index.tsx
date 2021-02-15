import { NativeModules } from 'react-native';

type ThubRnFingerprintType = {
  multiply(a: number, b: number): Promise<number>;
};

const { ThubRnFingerprint } = NativeModules;

export default ThubRnFingerprint as ThubRnFingerprintType;
