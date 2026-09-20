import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IContentType } from '../content-type.model';
import { ContentTypeService } from '../service/content-type.service';

import { ContentTypeFormGroup, ContentTypeFormService } from './content-type-form.service';

@Component({
  selector: 'sys-content-type-update',
  templateUrl: './content-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ContentTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  contentType: IContentType | null = null;

  protected contentTypeService = inject(ContentTypeService);
  protected contentTypeFormService = inject(ContentTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContentTypeFormGroup = this.contentTypeFormService.createContentTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contentType }) => {
      this.contentType = contentType;
      if (contentType) {
        this.updateForm(contentType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const contentType = this.contentTypeFormService.getContentType(this.editForm);
    if (contentType.id === null) {
      this.subscribeToSaveResponse(this.contentTypeService.create(contentType));
    } else {
      this.subscribeToSaveResponse(this.contentTypeService.update(contentType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IContentType | null>): void {
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

  protected updateForm(contentType: IContentType): void {
    this.contentType = contentType;
    this.contentTypeFormService.resetForm(this.editForm, contentType);
  }
}
