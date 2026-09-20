import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IOtpAuth } from '../otp-auth.model';

@Component({
  selector: 'sys-otp-auth-detail',
  templateUrl: './otp-auth-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class OtpAuthDetail {
  readonly otpAuth = input<IOtpAuth | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
