import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMechPurpose } from '../contact-mech-purpose.model';
import { ContactMechPurposeService } from '../service/contact-mech-purpose.service';

import { ContactMechPurposeFormService } from './contact-mech-purpose-form.service';
import { ContactMechPurposeUpdate } from './contact-mech-purpose-update';

describe('ContactMechPurpose Management Update Component', () => {
  let comp: ContactMechPurposeUpdate;
  let fixture: ComponentFixture<ContactMechPurposeUpdate>;
  let activatedRoute: ActivatedRoute;
  let contactMechPurposeFormService: ContactMechPurposeFormService;
  let contactMechPurposeService: ContactMechPurposeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ContactMechPurposeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactMechPurposeFormService = TestBed.inject(ContactMechPurposeFormService);
    contactMechPurposeService = TestBed.inject(ContactMechPurposeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const contactMechPurpose: IContactMechPurpose = { id: 10745 };

      activatedRoute.data = of({ contactMechPurpose });
      comp.ngOnInit();

      expect(comp.contactMechPurpose).toEqual(contactMechPurpose);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContactMechPurpose>();
      const contactMechPurpose = { id: 25033 };
      vi.spyOn(contactMechPurposeFormService, 'getContactMechPurpose').mockReturnValue(contactMechPurpose);
      vi.spyOn(contactMechPurposeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMechPurpose });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contactMechPurpose);
      saveSubject.complete();

      // THEN
      expect(contactMechPurposeFormService.getContactMechPurpose).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactMechPurposeService.update).toHaveBeenCalledWith(expect.objectContaining(contactMechPurpose));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContactMechPurpose>();
      const contactMechPurpose = { id: 25033 };
      vi.spyOn(contactMechPurposeFormService, 'getContactMechPurpose').mockReturnValue({ id: null });
      vi.spyOn(contactMechPurposeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMechPurpose: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contactMechPurpose);
      saveSubject.complete();

      // THEN
      expect(contactMechPurposeFormService.getContactMechPurpose).toHaveBeenCalled();
      expect(contactMechPurposeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IContactMechPurpose>();
      const contactMechPurpose = { id: 25033 };
      vi.spyOn(contactMechPurposeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMechPurpose });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactMechPurposeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
