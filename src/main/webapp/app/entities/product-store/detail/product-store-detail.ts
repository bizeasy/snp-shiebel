import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { DataUtils } from 'app/core/util';
import { Alert, AlertError } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { TranslateDirective } from 'app/shared/language';
import { IProductStore } from '../product-store.model';

@Component({
  selector: 'sys-product-store-detail',
  templateUrl: './product-store-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductStoreDetail {
  readonly productStore = input<IProductStore | null>(null);

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined, blobType?: BlobType): void {
    this.dataUtils.openFile(base64String, contentType, blobType);
  }

  previousState(): void {
    globalThis.history.back();
  }
}
