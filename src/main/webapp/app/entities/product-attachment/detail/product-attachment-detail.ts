import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductAttachment } from '../product-attachment.model';

@Component({
  selector: 'sys-product-attachment-detail',
  templateUrl: './product-attachment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductAttachmentDetail {
  readonly productAttachment = input<IProductAttachment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
