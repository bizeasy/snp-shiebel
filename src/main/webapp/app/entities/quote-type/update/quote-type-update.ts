import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteType } from '../quote-type.model';
import { QuoteTypeService } from '../service/quote-type.service';

import { QuoteTypeFormGroup, QuoteTypeFormService } from './quote-type-form.service';

@Component({
  selector: 'sys-quote-type-update',
  templateUrl: './quote-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class QuoteTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  quoteType: IQuoteType | null = null;

  protected quoteTypeService = inject(QuoteTypeService);
  protected quoteTypeFormService = inject(QuoteTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuoteTypeFormGroup = this.quoteTypeFormService.createQuoteTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quoteType }) => {
      this.quoteType = quoteType;
      if (quoteType) {
        this.updateForm(quoteType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const quoteType = this.quoteTypeFormService.getQuoteType(this.editForm);
    if (quoteType.id === null) {
      this.subscribeToSaveResponse(this.quoteTypeService.create(quoteType));
    } else {
      this.subscribeToSaveResponse(this.quoteTypeService.update(quoteType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IQuoteType | null>): void {
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

  protected updateForm(quoteType: IQuoteType): void {
    this.quoteType = quoteType;
    this.quoteTypeFormService.resetForm(this.editForm, quoteType);
  }
}
