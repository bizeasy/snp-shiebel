import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductAttachment } from '../product-attachment.model';
import { ProductAttachmentService } from '../service/product-attachment.service';

import { ProductAttachmentFormGroup, ProductAttachmentFormService } from './product-attachment-form.service';

@Component({
  selector: 'sys-product-attachment-update',
  templateUrl: './product-attachment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductAttachmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  productAttachment: IProductAttachment | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  attachmentsSharedCollection = signal<IAttachment[]>([]);

  protected productAttachmentService = inject(ProductAttachmentService);
  protected productAttachmentFormService = inject(ProductAttachmentFormService);
  protected productService = inject(ProductService);
  protected attachmentService = inject(AttachmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductAttachmentFormGroup = this.productAttachmentFormService.createProductAttachmentFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareAttachment = (o1: IAttachment | null, o2: IAttachment | null): boolean => this.attachmentService.compareAttachment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productAttachment }) => {
      this.productAttachment = productAttachment;
      if (productAttachment) {
        this.updateForm(productAttachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productAttachment = this.productAttachmentFormService.getProductAttachment(this.editForm);
    if (productAttachment.id === null) {
      this.subscribeToSaveResponse(this.productAttachmentService.create(productAttachment));
    } else {
      this.subscribeToSaveResponse(this.productAttachmentService.update(productAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductAttachment | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(productAttachment: IProductAttachment): void {
    this.productAttachment = productAttachment;
    this.productAttachmentFormService.resetForm(this.editForm, productAttachment);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productAttachment.product),
    );
    this.attachmentsSharedCollection.update(attachments =>
      this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, productAttachment.attachment),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productAttachment?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.attachmentService
      .query()
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, this.productAttachment?.attachment),
        ),
      )
      .subscribe((attachments: IAttachment[]) => this.attachmentsSharedCollection.set(attachments));
  }
}
