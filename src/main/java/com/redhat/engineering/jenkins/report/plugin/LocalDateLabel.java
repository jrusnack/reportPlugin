package com.redhat.engineering.jenkins.report.plugin;

import org.joda.time.LocalDate;

/**
 * Graph label showing the build date.
 *
 * @author Ulli Hafner
 */
public class LocalDateLabel implements Comparable<LocalDateLabel> {
    private final LocalDate date;

    /**
     * Creates a new instance of {@link LocalDateLabel}.
     *
     * @param date
     *            the date of the build
     */
    public LocalDateLabel(final LocalDate date) {
        this.date = date;
    }

    /** {@inheritDoc} */
    public int compareTo(final LocalDateLabel o) {
        return date.compareTo(o.date);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return date.toString("MM-dd");
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LocalDateLabel other = (LocalDateLabel)obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        }
        else if (!date.equals(other.date)) {
            return false;
        }
        return true;
    }
}

