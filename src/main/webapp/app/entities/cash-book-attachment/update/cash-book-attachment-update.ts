import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { ICashBook } from 'app/entities/cash-book/cash-book.model';
import { CashBookService } from 'app/entities/cash-book/service/cash-book.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICashBookAttachment } from '../cash-book-attachment.model';
import { CashBookAttachmentService } from '../service/cash-book-attachment.service';

import { CashBookAttachmentFormGroup, CashBookAttachmentFormService } from './cash-book-attachment-form.service';

@Component({
  selector: 'sys-cash-book-attachment-update',
  templateUrl: './cash-book-attachment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CashBookAttachmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  cashBookAttachment: ICashBookAttachment | null = null;

  cashBooksSharedCollection = signal<ICashBook[]>([]);
  attachmentsSharedCollection = signal<IAttachment[]>([]);

  protected cashBookAttachmentService = inject(CashBookAttachmentService);
  protected cashBookAttachmentFormService = inject(CashBookAttachmentFormService);
  protected cashBookService = inject(CashBookService);
  protected attachmentService = inject(AttachmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CashBookAttachmentFormGroup = this.cashBookAttachmentFormService.createCashBookAttachmentFormGroup();

  compareCashBook = (o1: ICashBook | null, o2: ICashBook | null): boolean => this.cashBookService.compareCashBook(o1, o2);

  compareAttachment = (o1: IAttachment | null, o2: IAttachment | null): boolean => this.attachmentService.compareAttachment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashBookAttachment }) => {
      this.cashBookAttachment = cashBookAttachment;
      if (cashBookAttachment) {
        this.updateForm(cashBookAttachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const cashBookAttachment = this.cashBookAttachmentFormService.getCashBookAttachment(this.editForm);
    if (cashBookAttachment.id === null) {
      this.subscribeToSaveResponse(this.cashBookAttachmentService.create(cashBookAttachment));
    } else {
      this.subscribeToSaveResponse(this.cashBookAttachmentService.update(cashBookAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICashBookAttachment | null>): void {
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

  protected updateForm(cashBookAttachment: ICashBookAttachment): void {
    this.cashBookAttachment = cashBookAttachment;
    this.cashBookAttachmentFormService.resetForm(this.editForm, cashBookAttachment);

    this.cashBooksSharedCollection.update(cashBooks =>
      this.cashBookService.addCashBookToCollectionIfMissing<ICashBook>(cashBooks, cashBookAttachment.cashBook),
    );
    this.attachmentsSharedCollection.update(attachments =>
      this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, cashBookAttachment.attachment),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cashBookService
      .query()
      .pipe(map((res: HttpResponse<ICashBook[]>) => res.body ?? []))
      .pipe(
        map((cashBooks: ICashBook[]) =>
          this.cashBookService.addCashBookToCollectionIfMissing<ICashBook>(cashBooks, this.cashBookAttachment?.cashBook),
        ),
      )
      .subscribe((cashBooks: ICashBook[]) => this.cashBooksSharedCollection.set(cashBooks));

    this.attachmentService
      .query()
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, this.cashBookAttachment?.attachment),
        ),
      )
      .subscribe((attachments: IAttachment[]) => this.attachmentsSharedCollection.set(attachments));
  }
}
