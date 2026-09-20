import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMechType } from '../contact-mech-type.model';
import { ContactMechTypeService } from '../service/contact-mech-type.service';

import { ContactMechTypeFormService } from './contact-mech-type-form.service';
import { ContactMechTypeUpdate } from './contact-mech-type-update';

describe('ContactMechType Management Update Component', () => {
  let comp: ContactMechTypeUpdate;
  let fixture: ComponentFixture<ContactMechTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let contactMechTypeFormService: ContactMechTypeFormService;
  let contactMechTypeService: ContactMechTypeService;

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

    fixture = TestBed.createComponent(ContactMechTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactMechTypeFormService = TestBed.inject(ContactMechTypeFormService);
    contactMechTypeService = TestBed.inject(ContactMechTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const contactMechType: IContactMechType = { id: 22225 };

      activatedRoute.data = of({ contactMechType });
      comp.ngOnInit();

      expect(comp.contactMechType).toEqual(contactMechType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContactMechType>();
      const contactMechType = { id: 10228 };
      vi.spyOn(contactMechTypeFormService, 'getContactMechType').mockReturnValue(contactMechType);
      vi.spyOn(contactMechTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMechType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contactMechType);
      saveSubject.complete();

      // THEN
      expect(contactMechTypeFormService.getContactMechType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactMechTypeService.update).toHaveBeenCalledWith(expect.objectContaining(contactMechType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContactMechType>();
      const contactMechType = { id: 10228 };
      vi.spyOn(contactMechTypeFormService, 'getContactMechType').mockReturnValue({ id: null });
      vi.spyOn(contactMechTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMechType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contactMechType);
      saveSubject.complete();

      // THEN
      expect(contactMechTypeFormService.getContactMechType).toHaveBeenCalled();
      expect(contactMechTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IContactMechType>();
      const contactMechType = { id: 10228 };
      vi.spyOn(contactMechTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMechType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactMechTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
