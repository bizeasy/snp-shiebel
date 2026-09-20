import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteAttachment } from '../quote-attachment.model';
import { QuoteAttachmentService } from '../service/quote-attachment.service';

import { QuoteAttachmentFormGroup, QuoteAttachmentFormService } from './quote-attachment-form.service';

@Component({
  selector: 'sys-quote-attachment-update',
  templateUrl: './quote-attachment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class QuoteAttachmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  quoteAttachment: IQuoteAttachment | null = null;

  quotesSharedCollection = signal<IQuote[]>([]);
  attachmentsSharedCollection = signal<IAttachment[]>([]);

  protected quoteAttachmentService = inject(QuoteAttachmentService);
  protected quoteAttachmentFormService = inject(QuoteAttachmentFormService);
  protected quoteService = inject(QuoteService);
  protected attachmentService = inject(AttachmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuoteAttachmentFormGroup = this.quoteAttachmentFormService.createQuoteAttachmentFormGroup();

  compareQuote = (o1: IQuote | null, o2: IQuote | null): boolean => this.quoteService.compareQuote(o1, o2);

  compareAttachment = (o1: IAttachment | null, o2: IAttachment | null): boolean => this.attachmentService.compareAttachment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quoteAttachment }) => {
      this.quoteAttachment = quoteAttachment;
      if (quoteAttachment) {
        this.updateForm(quoteAttachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const quoteAttachment = this.quoteAttachmentFormService.getQuoteAttachment(this.editForm);
    if (quoteAttachment.id === null) {
      this.subscribeToSaveResponse(this.quoteAttachmentService.create(quoteAttachment));
    } else {
      this.subscribeToSaveResponse(this.quoteAttachmentService.update(quoteAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IQuoteAttachment | null>): void {
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

  protected updateForm(quoteAttachment: IQuoteAttachment): void {
    this.quoteAttachment = quoteAttachment;
    this.quoteAttachmentFormService.resetForm(this.editForm, quoteAttachment);

    this.quotesSharedCollection.update(quotes => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, quoteAttachment.quote));
    this.attachmentsSharedCollection.update(attachments =>
      this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, quoteAttachment.attachment),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quoteService
      .query()
      .pipe(map((res: HttpResponse<IQuote[]>) => res.body ?? []))
      .pipe(map((quotes: IQuote[]) => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, this.quoteAttachment?.quote)))
      .subscribe((quotes: IQuote[]) => this.quotesSharedCollection.set(quotes));

    this.attachmentService
      .query()
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, this.quoteAttachment?.attachment),
        ),
      )
      .subscribe((attachments: IAttachment[]) => this.attachmentsSharedCollection.set(attachments));
  }
}
