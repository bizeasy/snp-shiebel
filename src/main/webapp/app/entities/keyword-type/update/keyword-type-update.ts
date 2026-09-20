import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IKeywordType } from '../keyword-type.model';
import { KeywordTypeService } from '../service/keyword-type.service';

import { KeywordTypeFormGroup, KeywordTypeFormService } from './keyword-type-form.service';

@Component({
  selector: 'sys-keyword-type-update',
  templateUrl: './keyword-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class KeywordTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  keywordType: IKeywordType | null = null;

  protected keywordTypeService = inject(KeywordTypeService);
  protected keywordTypeFormService = inject(KeywordTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: KeywordTypeFormGroup = this.keywordTypeFormService.createKeywordTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ keywordType }) => {
      this.keywordType = keywordType;
      if (keywordType) {
        this.updateForm(keywordType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const keywordType = this.keywordTypeFormService.getKeywordType(this.editForm);
    if (keywordType.id === null) {
      this.subscribeToSaveResponse(this.keywordTypeService.create(keywordType));
    } else {
      this.subscribeToSaveResponse(this.keywordTypeService.update(keywordType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IKeywordType | null>): void {
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

  protected updateForm(keywordType: IKeywordType): void {
    this.keywordType = keywordType;
    this.keywordTypeFormService.resetForm(this.editForm, keywordType);
  }
}
