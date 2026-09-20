import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { CustRequestService } from 'app/entities/cust-request/service/cust-request.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestAttachment } from '../cust-request-attachment.model';
import { CustRequestAttachmentService } from '../service/cust-request-attachment.service';

import { CustRequestAttachmentFormGroup, CustRequestAttachmentFormService } from './cust-request-attachment-form.service';

@Component({
  selector: 'sys-cust-request-attachment-update',
  templateUrl: './cust-request-attachment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CustRequestAttachmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  custRequestAttachment: ICustRequestAttachment | null = null;

  custRequestsSharedCollection = signal<ICustRequest[]>([]);
  attachmentsSharedCollection = signal<IAttachment[]>([]);

  protected custRequestAttachmentService = inject(CustRequestAttachmentService);
  protected custRequestAttachmentFormService = inject(CustRequestAttachmentFormService);
  protected custRequestService = inject(CustRequestService);
  protected attachmentService = inject(AttachmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustRequestAttachmentFormGroup = this.custRequestAttachmentFormService.createCustRequestAttachmentFormGroup();

  compareCustRequest = (o1: ICustRequest | null, o2: ICustRequest | null): boolean => this.custRequestService.compareCustRequest(o1, o2);

  compareAttachment = (o1: IAttachment | null, o2: IAttachment | null): boolean => this.attachmentService.compareAttachment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ custRequestAttachment }) => {
      this.custRequestAttachment = custRequestAttachment;
      if (custRequestAttachment) {
        this.updateForm(custRequestAttachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const custRequestAttachment = this.custRequestAttachmentFormService.getCustRequestAttachment(this.editForm);
    if (custRequestAttachment.id === null) {
      this.subscribeToSaveResponse(this.custRequestAttachmentService.create(custRequestAttachment));
    } else {
      this.subscribeToSaveResponse(this.custRequestAttachmentService.update(custRequestAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustRequestAttachment | null>): void {
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

  protected updateForm(custRequestAttachment: ICustRequestAttachment): void {
    this.custRequestAttachment = custRequestAttachment;
    this.custRequestAttachmentFormService.resetForm(this.editForm, custRequestAttachment);

    this.custRequestsSharedCollection.update(custRequests =>
      this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, custRequestAttachment.custRequest),
    );
    this.attachmentsSharedCollection.update(attachments =>
      this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, custRequestAttachment.attachment),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.custRequestService
      .query()
      .pipe(map((res: HttpResponse<ICustRequest[]>) => res.body ?? []))
      .pipe(
        map((custRequests: ICustRequest[]) =>
          this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, this.custRequestAttachment?.custRequest),
        ),
      )
      .subscribe((custRequests: ICustRequest[]) => this.custRequestsSharedCollection.set(custRequests));

    this.attachmentService
      .query()
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, this.custRequestAttachment?.attachment),
        ),
      )
      .subscribe((attachments: IAttachment[]) => this.attachmentsSharedCollection.set(attachments));
  }
}
