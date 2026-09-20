import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ContactMechPurpose e2e test', () => {
  const contactMechPurposePageUrl = '/contact-mech-purpose';
  let username: string;
  let password: string;
  const contactMechPurposeSample = { name: 'rapidly married' };

  let contactMechPurpose;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contact-mech-purposes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contact-mech-purposes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contact-mech-purposes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contactMechPurpose) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contact-mech-purposes/${contactMechPurpose.id}`,
      }).then(() => {
        contactMechPurpose = undefined;
      });
    }
  });

  it('ContactMechPurposes menu should load ContactMechPurposes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contact-mech-purpose');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContactMechPurpose').should('exist');
    cy.location('pathname').should('eq', contactMechPurposePageUrl);
  });

  describe('ContactMechPurpose page', () => {
    it('should have translated page title', () => {
      cy.visit(contactMechPurposePageUrl);
      cy.getEntityHeading('ContactMechPurpose').should('not.contain', 'snpApp.contactMechPurpose.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contactMechPurposePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContactMechPurpose page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${contactMechPurposePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ContactMechPurpose');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPurposePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contact-mech-purposes',
          body: contactMechPurposeSample,
        }).then(({ body }) => {
          contactMechPurpose = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contact-mech-purposes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [contactMechPurpose],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contactMechPurposePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContactMechPurpose page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contactMechPurpose');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPurposePageUrl);
      });

      it('edit button click should load edit ContactMechPurpose page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContactMechPurpose');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPurposePageUrl);
      });

      it('edit button click should load edit ContactMechPurpose page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContactMechPurpose');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPurposePageUrl);
      });

      it('last delete button click should delete instance of ContactMechPurpose', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contactMechPurpose').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPurposePageUrl);

        contactMechPurpose = undefined;
      });
    });
  });

  describe('new ContactMechPurpose page', () => {
    beforeEach(() => {
      cy.visit(contactMechPurposePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContactMechPurpose');
    });

    it('should create an instance of ContactMechPurpose', () => {
      cy.get(`[data-cy="name"]`).type('considerate now provided');
      cy.get(`[data-cy="name"]`).should('have.value', 'considerate now provided');

      cy.get(`[data-cy="description"]`).type('slide geez');
      cy.get(`[data-cy="description"]`).should('have.value', 'slide geez');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contactMechPurpose = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', contactMechPurposePageUrl);
    });
  });
});
