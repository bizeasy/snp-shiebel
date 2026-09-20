import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IContactMechType } from '../contact-mech-type.model';
import { ContactMechTypeService } from '../service/contact-mech-type.service';

import { ContactMechTypeFormGroup, ContactMechTypeFormService } from './contact-mech-type-form.service';

@Component({
  selector: 'sys-contact-mech-type-update',
  templateUrl: './contact-mech-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ContactMechTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  contactMechType: IContactMechType | null = null;

  protected contactMechTypeService = inject(ContactMechTypeService);
  protected contactMechTypeFormService = inject(ContactMechTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContactMechTypeFormGroup = this.contactMechTypeFormService.createContactMechTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactMechType }) => {
      this.contactMechType = contactMechType;
      if (contactMechType) {
        this.updateForm(contactMechType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const contactMechType = this.contactMechTypeFormService.getContactMechType(this.editForm);
    if (contactMechType.id === null) {
      this.subscribeToSaveResponse(this.contactMechTypeService.create(contactMechType));
    } else {
      this.subscribeToSaveResponse(this.contactMechTypeService.update(contactMechType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IContactMechType | null>): void {
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

  protected updateForm(contactMechType: IContactMechType): void {
    this.contactMechType = contactMechType;
    this.contactMechTypeFormService.resetForm(this.editForm, contactMechType);
  }
}
