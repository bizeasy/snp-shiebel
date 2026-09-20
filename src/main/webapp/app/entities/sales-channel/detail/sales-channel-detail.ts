import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ISalesChannel } from '../sales-channel.model';

@Component({
  selector: 'sys-sales-channel-detail',
  templateUrl: './sales-channel-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class SalesChannelDetail {
  readonly salesChannel = input<ISalesChannel | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
