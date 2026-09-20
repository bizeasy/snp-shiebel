import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IFileShare } from '../file-share.model';

@Component({
  selector: 'sys-file-share-detail',
  templateUrl: './file-share-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class FileShareDetail {
  readonly fileShare = input<IFileShare | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
