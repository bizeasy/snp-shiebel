import dayjs from 'dayjs/esm';

import { IOtpAuth, NewOtpAuth } from './otp-auth.model';

export const sampleWithRequiredData: IOtpAuth = {
  id: 4343,
};

export const sampleWithPartialData: IOtpAuth = {
  id: 32087,
  otp: 'considerin',
  verificationSuccess: false,
  otpExpired: true,
  otpSentTime: dayjs('2023-12-13T22:37'),
  otpResetCounter: 7989,
  otpActiveTime: 29757,
};

export const sampleWithFullData: IOtpAuth = {
  id: 17811,
  otp: 'ethyl',
  otpSent: false,
  verificationSuccess: false,
  otpExpired: false,
  otpSentTime: dayjs('2023-12-13T21:50'),
  sentCounter: 21393,
  failCounter: 27222,
  otpResetCounter: 26683,
  maxResend: 1277,
  maxReset: 18594,
  maxFailures: 28133,
  otpActiveTime: 17470,
};

export const sampleWithNewData: NewOtpAuth = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
