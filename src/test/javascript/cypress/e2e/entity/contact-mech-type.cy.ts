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

describe('ContactMechType e2e test', () => {
  const contactMechTypePageUrl = '/contact-mech-type';
  let username: string;
  let password: string;
  const contactMechTypeSample = { name: 'whether help optimistical' };

  let contactMechType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contact-mech-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contact-mech-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contact-mech-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contactMechType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contact-mech-types/${contactMechType.id}`,
      }).then(() => {
        contactMechType = undefined;
      });
    }
  });

  it('ContactMechTypes menu should load ContactMechTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contact-mech-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContactMechType').should('exist');
    cy.location('pathname').should('eq', contactMechTypePageUrl);
  });

  describe('ContactMechType page', () => {
    it('should have translated page title', () => {
      cy.visit(contactMechTypePageUrl);
      cy.getEntityHeading('ContactMechType').should('not.contain', 'snpApp.contactMechType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contactMechTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContactMechType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${contactMechTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ContactMechType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contact-mech-types',
          body: contactMechTypeSample,
        }).then(({ body }) => {
          contactMechType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contact-mech-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [contactMechType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contactMechTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContactMechType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contactMechType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechTypePageUrl);
      });

      it('edit button click should load edit ContactMechType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContactMechType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechTypePageUrl);
      });

      it('edit button click should load edit ContactMechType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContactMechType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechTypePageUrl);
      });

      it('last delete button click should delete instance of ContactMechType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contactMechType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechTypePageUrl);

        contactMechType = undefined;
      });
    });
  });

  describe('new ContactMechType page', () => {
    beforeEach(() => {
      cy.visit(contactMechTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContactMechType');
    });

    it('should create an instance of ContactMechType', () => {
      cy.get(`[data-cy="name"]`).type('badly');
      cy.get(`[data-cy="name"]`).should('have.value', 'badly');

      cy.get(`[data-cy="description"]`).type('obvious fluctuate decode');
      cy.get(`[data-cy="description"]`).should('have.value', 'obvious fluctuate decode');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contactMechType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', contactMechTypePageUrl);
    });
  });
});
