import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { ICustRequestItem } from 'app/entities/cust-request-item/cust-request-item.model';
import { CustRequestItemService } from 'app/entities/cust-request-item/service/cust-request-item.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestItemAttachment } from '../cust-request-item-attachment.model';
import { CustRequestItemAttachmentService } from '../service/cust-request-item-attachment.service';

import { CustRequestItemAttachmentFormGroup, CustRequestItemAttachmentFormService } from './cust-request-item-attachment-form.service';

@Component({
  selector: 'sys-cust-request-item-attachment-update',
  templateUrl: './cust-request-item-attachment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CustRequestItemAttachmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  custRequestItemAttachment: ICustRequestItemAttachment | null = null;

  custRequestItemsSharedCollection = signal<ICustRequestItem[]>([]);
  attachmentsSharedCollection = signal<IAttachment[]>([]);

  protected custRequestItemAttachmentService = inject(CustRequestItemAttachmentService);
  protected custRequestItemAttachmentFormService = inject(CustRequestItemAttachmentFormService);
  protected custRequestItemService = inject(CustRequestItemService);
  protected attachmentService = inject(AttachmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustRequestItemAttachmentFormGroup = this.custRequestItemAttachmentFormService.createCustRequestItemAttachmentFormGroup();

  compareCustRequestItem = (o1: ICustRequestItem | null, o2: ICustRequestItem | null): boolean =>
    this.custRequestItemService.compareCustRequestItem(o1, o2);

  compareAttachment = (o1: IAttachment | null, o2: IAttachment | null): boolean => this.attachmentService.compareAttachment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ custRequestItemAttachment }) => {
      this.custRequestItemAttachment = custRequestItemAttachment;
      if (custRequestItemAttachment) {
        this.updateForm(custRequestItemAttachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const custRequestItemAttachment = this.custRequestItemAttachmentFormService.getCustRequestItemAttachment(this.editForm);
    if (custRequestItemAttachment.id === null) {
      this.subscribeToSaveResponse(this.custRequestItemAttachmentService.create(custRequestItemAttachment));
    } else {
      this.subscribeToSaveResponse(this.custRequestItemAttachmentService.update(custRequestItemAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustRequestItemAttachment | null>): void {
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

  protected updateForm(custRequestItemAttachment: ICustRequestItemAttachment): void {
    this.custRequestItemAttachment = custRequestItemAttachment;
    this.custRequestItemAttachmentFormService.resetForm(this.editForm, custRequestItemAttachment);

    this.custRequestItemsSharedCollection.update(custRequestItems =>
      this.custRequestItemService.addCustRequestItemToCollectionIfMissing<ICustRequestItem>(
        custRequestItems,
        custRequestItemAttachment.custRequestItem,
      ),
    );
    this.attachmentsSharedCollection.update(attachments =>
      this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, custRequestItemAttachment.attachment),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.custRequestItemService
      .query()
      .pipe(map((res: HttpResponse<ICustRequestItem[]>) => res.body ?? []))
      .pipe(
        map((custRequestItems: ICustRequestItem[]) =>
          this.custRequestItemService.addCustRequestItemToCollectionIfMissing<ICustRequestItem>(
            custRequestItems,
            this.custRequestItemAttachment?.custRequestItem,
          ),
        ),
      )
      .subscribe((custRequestItems: ICustRequestItem[]) => this.custRequestItemsSharedCollection.set(custRequestItems));

    this.attachmentService
      .query()
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, this.custRequestItemAttachment?.attachment),
        ),
      )
      .subscribe((attachments: IAttachment[]) => this.attachmentsSharedCollection.set(attachments));
  }
}
