import dayjs from 'dayjs/esm';

import { IParty } from 'app/entities/party/party.model';

export interface IOtpAuth {
  id: number;
  otp?: string | null;
  otpSent?: boolean | null;
  verificationSuccess?: boolean | null;
  otpExpired?: boolean | null;
  otpSentTime?: dayjs.Dayjs | null;
  sentCounter?: number | null;
  failCounter?: number | null;
  otpResetCounter?: number | null;
  maxResend?: number | null;
  maxReset?: number | null;
  maxFailures?: number | null;
  otpActiveTime?: number | null;
  party?: IParty | null;
}

export type NewOtpAuth = Omit<IOtpAuth, 'id'> & { id: null };
