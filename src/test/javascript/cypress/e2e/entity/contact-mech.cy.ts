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

describe('ContactMech e2e test', () => {
  const contactMechPageUrl = '/contact-mech';
  let username: string;
  let password: string;
  const contactMechSample = {};

  let contactMech;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contact-meches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contact-meches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contact-meches/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contactMech) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contact-meches/${contactMech.id}`,
      }).then(() => {
        contactMech = undefined;
      });
    }
  });

  it('ContactMeches menu should load ContactMeches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contact-mech');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContactMech').should('exist');
    cy.location('pathname').should('eq', contactMechPageUrl);
  });

  describe('ContactMech page', () => {
    it('should have translated page title', () => {
      cy.visit(contactMechPageUrl);
      cy.getEntityHeading('ContactMech').should('not.contain', 'snpApp.contactMech.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contactMechPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContactMech page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${contactMechPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ContactMech');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contact-meches',
          body: contactMechSample,
        }).then(({ body }) => {
          contactMech = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contact-meches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/contact-meches?page=0&size=20>; rel="last",<http://localhost/api/contact-meches?page=0&size=20>; rel="first"',
              },
              body: [contactMech],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contactMechPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContactMech page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contactMech');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPageUrl);
      });

      it('edit button click should load edit ContactMech page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContactMech');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPageUrl);
      });

      it('edit button click should load edit ContactMech page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContactMech');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPageUrl);
      });

      it('last delete button click should delete instance of ContactMech', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contactMech').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contactMechPageUrl);

        contactMech = undefined;
      });
    });
  });

  describe('new ContactMech page', () => {
    beforeEach(() => {
      cy.visit(contactMechPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContactMech');
    });

    it('should create an instance of ContactMech', () => {
      cy.get(`[data-cy="infoString"]`).type('and wherever reopen');
      cy.get(`[data-cy="infoString"]`).should('have.value', 'and wherever reopen');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contactMech = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', contactMechPageUrl);
    });
  });
});
